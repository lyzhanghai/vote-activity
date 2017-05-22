package com.lyl.outsourcing.activity.dao

import com.lyl.outsourcing.activity.common.CacheConst
import com.lyl.outsourcing.activity.common.Global
import com.lyl.outsourcing.activity.common.XUtil
import com.lyl.outsourcing.activity.dao.mapper.RaffleItemMapper
import com.lyl.outsourcing.activity.entity.RaffleItem
import com.lyl.outsourcing.activity.entity.RaffleItemExample
import com.lyl.outsourcing.activity.entity.RaffleItemWithNum
import com.lyl.outsourcing.activity.exception.DaoException
import org.apache.ibatis.session.SqlSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * Created by liyilin on 2017/4/21.
 */
@Repository
class RaffleItemDao extends BaseDao<RaffleItem> implements RaffleItemMapper {

    @Autowired
    @Delegate
    private RaffleItemMapper mapper

    @Autowired
    private SqlSession sqlSession

    RaffleItemDao() {
        super(RaffleItem.class)
    }

    int insert(RaffleItem raffleItem) {
        int row = mapper.insert(raffleItem)

        // 添加数量
        Global.redisTemplate.opsForValue().set(CacheConst.raffleItemNum(raffleItem.id), raffleItem.totalNum.toString())

        row
    }

    int updateByPrimaryKey(RaffleItem raffleItem) {
        def row = mapper.updateByPrimaryKey(raffleItem)
        RaffleItem persist = mapper.selectByPrimaryKey(raffleItem.id)
        // 修改实际奖项数量
        decrItemNum( raffleItem.id, persist.totalNum - raffleItem.totalNum )
        row
    }

    int deleteByPrimaryKey(Long id) {
        def row = mapper.deleteByPrimaryKey(id)
        // 删除数量
        Global.redisTemplate.delete(CacheConst.raffleItemNum(id))
        row
    }

    /**
     * 奖品实际剩余数量
     * @param raffleItemId
     * @return
     */
    int getItemNum(long raffleItemId) {
        String str = Global.redisTemplate.opsForValue().get(CacheConst.raffleItemNum(raffleItemId))
        if (str != null) return Integer.parseInt( str )
        else return 0
    }

    void decrItemNum(long raffleItemId, int num) {
        int persistNum = getItemNum(raffleItemId)
        if (num > persistNum)
            throw new DaoException("抽奖项[ID=${raffleItemId}]数量少于${num}")
        Global.redisTemplate.opsForValue().increment(CacheConst.raffleItemNum(raffleItemId), -num)
    }

    RaffleItemWithNum selectByPrimaryKeyWithNum(Long id) {
        RaffleItem raffleItem = mapper.selectByPrimaryKey(id)
        RaffleItemWithNum raffleItemWithNum = new RaffleItemWithNum()
        XUtil.copy(raffleItem, raffleItemWithNum)
        raffleItemWithNum.num = getItemNum(id)
        raffleItemWithNum
    }

    List<RaffleItemWithNum> selectByRaffleIdWithNum(long raffleId) {
        List<RaffleItem> list = selectByRaffleId(raffleId)
        List<RaffleItemWithNum> itemWithNumList = new ArrayList<>(list.size())

        for (RaffleItem raffleItem : list) {
            RaffleItemWithNum raffleItemWithNum = new RaffleItemWithNum()
            XUtil.copy(raffleItem, raffleItemWithNum)
            raffleItemWithNum.num = getItemNum(raffleItem.id)
            itemWithNumList << raffleItemWithNum
        }
        itemWithNumList
    }

    List<RaffleItem> selectByRaffleId(long raffleId) {
        RaffleItemExample example = new RaffleItemExample()
        example.or().andRaffleIdEqualTo(raffleId)
        mapper.selectByExample(example)
    }
}
