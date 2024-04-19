package cn.homyit.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OccupiedSiteVO {
    String roomNum;
    boolean ifOccupied;
}
