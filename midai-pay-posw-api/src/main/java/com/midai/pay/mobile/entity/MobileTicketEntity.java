package com.midai.pay.mobile.entity;

import java.util.List;

import com.midai.pay.mobile.AppBaseEntity;
import com.midai.pay.mobile.vo.EticketSignVo;

import lombok.Data;
import lombok.EqualsAndHashCode;
@EqualsAndHashCode(callSuper=false)
@Data
public class MobileTicketEntity extends AppBaseEntity {

	private List<EticketSignVo> LISTS;
}
