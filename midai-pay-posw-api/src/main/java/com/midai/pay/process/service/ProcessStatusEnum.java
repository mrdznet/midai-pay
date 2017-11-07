package com.midai.pay.process.service;

public enum ProcessStatusEnum {
	car_process___create_order,
	car_process___order_first_evaluate,
	car_process___car_first_evaluate,
	car_process___order_second_evaluate,
	car_process___car_second_evaluate,
	car_process___order_final_evaluate,
	car_process___outer_visit,
	car_process___risk_control,
	car_process___gm_approve,
	car_process___order_sp__edit,
	
	done,
	unknown
}
