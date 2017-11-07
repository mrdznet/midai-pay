package com.midai.pay.web.controller.secretkey;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.pay.secretkey.service.SecretkeyService;
import com.midai.pay.secretkey.vo.SecretkeyVo;

@RestController
@ApiModel("密钥下载")
@RequestMapping("/secretkey")
public class SecretkeyController {

	@Reference
	public SecretkeyService secretkeyService;

	@ApiOperation("生成密钥")
	@PostMapping("/createkey/{facture}/{num}")
	public SecretkeyVo createSecretkey(@PathVariable("facture") String facture,
			 @PathVariable("num") int num) {
		return secretkeyService.createSecretkey(facture, num);
	}

	@ApiOperation("导出密钥")
	@GetMapping("/exportkey")
	public String exportSecretkey(HttpServletRequest request,
			HttpServletResponse response,SecretkeyVo svo) {
		String sk = secretkeyService.exportSecretkey(svo.getFacture(),
				svo.getBatch(), svo.getNum());
		// 设置response头信息
		response.reset();
		response.setContentType("text/plain"); // 改成输出txt文件
		response.setHeader("Content-disposition",
				"attachment; filename=exportkey.txt");

		ByteArrayInputStream bis = new ByteArrayInputStream(sk.getBytes());
		OutputStream out = null;
		// 创建工作簿并发送到浏览器
		try {

			out = response.getOutputStream();
			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while ((bytesRead = bis.read(buffer, 0, 1024)) != -1) {
				out.write(buffer, 0, bytesRead);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return null;
	}

}
