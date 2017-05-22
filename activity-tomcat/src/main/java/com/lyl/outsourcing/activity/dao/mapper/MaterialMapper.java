package com.lyl.outsourcing.activity.dao.mapper;

import com.lyl.outsourcing.activity.entity.Material;
import com.lyl.outsourcing.activity.entity.MaterialExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MaterialMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table r_material
     *
     * @mbg.generated
     */
    long countByExample(MaterialExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table r_material
     *
     * @mbg.generated
     */
    int deleteByExample(MaterialExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table r_material
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table r_material
     *
     * @mbg.generated
     */
    int insert(Material record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table r_material
     *
     * @mbg.generated
     */
    int insertSelective(Material record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table r_material
     *
     * @mbg.generated
     */
    List<Material> selectByExample(MaterialExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table r_material
     *
     * @mbg.generated
     */
    Material selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table r_material
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") Material record, @Param("example") MaterialExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table r_material
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") Material record, @Param("example") MaterialExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table r_material
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Material record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table r_material
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Material record);
}