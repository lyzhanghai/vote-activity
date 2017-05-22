package com.lyl.outsourcing.activity.dao

import com.lyl.outsourcing.activity.dao.mapper.MaterialMapper
import com.lyl.outsourcing.activity.entity.Material
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * Created by liyilin on 2017/4/21.
 */
@Repository
class MaterialDao extends BaseDao<Material> implements MaterialMapper {

    @Autowired
    @Delegate
    private MaterialMapper mapper

    MaterialDao() {
        super(Material.class)
    }
}
