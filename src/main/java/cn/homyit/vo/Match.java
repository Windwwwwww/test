package cn.homyit.vo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("t_match")
@AllArgsConstructor
@NoArgsConstructor
public class Match {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String label;
    private String name;
    private String introduction;
    private String picture;
    private String link;
}
