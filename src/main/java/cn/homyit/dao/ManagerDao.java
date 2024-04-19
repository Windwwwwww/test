package cn.homyit.dao;

import cn.homyit.dto.ManagerDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ManagerDao extends BaseMapper<ManagerDto> {
}
