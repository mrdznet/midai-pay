/**
 * Project Name:midai-pay-posw-service
 * File Name:MidaiActivityAspect.java
 * Package Name:com.midai.pay.user.config
 * Date:2016年12月8日上午11:16:33
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.activiti.service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.midai.pay.process.po.PendingTask;
import com.midai.pay.process.service.ProcessFacadeService;

import aj.org.objectweb.asm.ClassReader;
import aj.org.objectweb.asm.ClassVisitor;
import aj.org.objectweb.asm.Label;
import aj.org.objectweb.asm.MethodVisitor;
import aj.org.objectweb.asm.Opcodes;
import aj.org.objectweb.asm.Type;
import javassist.Modifier;
import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:MidaiActivityAspect <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年12月8日 上午11:16:33 <br/>
 * 
 * @author 陈勋
 * @version
 * @since JDK 1.7
 * @see
 */
@Aspect
@Slf4j
@Component
public class MidaiActivityAspect {

	@Autowired
	private ProcessFacadeService processFacadeService;

	@Before(value = "@annotation(activity)")
	public void exec(JoinPoint jp, MidaiActivity activity) throws Throwable {
		Object[] args = jp.getArgs();
		if (args != null) {
			String taskId = null;
			// defualt
			ExpressionParser parser = new SpelExpressionParser();
			EvaluationContext context = null;
			String exp = activity.task();
			Method m = null;
			Object target = jp.getTarget();
			String methodName = jp.getSignature().getName();
			Class[] parameterTypes = ((MethodSignature) jp.getSignature()).getMethod().getParameterTypes();
			m = target.getClass().getMethod(methodName, parameterTypes);

			if (args.length == 1 && exp.equals("#{taskId}")) {
				if(args[0] instanceof String){
					taskId= (String) args[0];
				}else{
					context = new StandardEvaluationContext(args[0]);
				}
				
			} else if (exp.startsWith("#{params")) {
				ParamContext paramContext = new ParamContext();

				String[] keys = getMethodParameterNamesByAsm4(target.getClass(), m);

				for (int i = 0; i < keys.length; i++) {
					paramContext.params.put(keys[i], args[i]);
				}

				context = new StandardEvaluationContext(paramContext);
			} else {
				Annotation[][] an = m.getParameterAnnotations();
				for (int i = 0; i < an.length; i++) {
					Annotation[] n = an[i];
					for (int k = 0; k < n.length; k++) {
						if (n[k] instanceof MidaiActivityParam) {
							MidaiActivityParam p = (MidaiActivityParam) n[k];
							if (args[i] instanceof String) {
								taskId = (String) args[i];
								break;
							}
							if(args[i]!=null){
								context = new StandardEvaluationContext(args[i]);
							}						
							break;
						}
					}
					if (!StringUtils.isEmpty(taskId) || context != null) {
						break;
					}

				}

			}
			if (StringUtils.isEmpty(taskId) && context != null) {
				Expression expression = parser.parseExpression(exp, new TemplateParserContext());
				taskId = expression.getValue(context, String.class);
			}
			if (StringUtils.isEmpty(taskId)) {
				// 任务编号为空，则不检查。
				return ;
		//		log.error("taskId  is empty !!!");
		//		throw new MidaiActivityException("任务编号为空.");
			}
			// 调用服务查询task是否存在
			PendingTask task = processFacadeService.getPendingTaskByTaskID(taskId);
			if (task == null) {
				log.error("task  is not exist !!!");
				throw new MidaiActivityException("该任务已处理.");
			}
		}

	}

	private static class ParamContext {
		public Map<String, Object> params = new HashMap<>();

	}

	/**
	 * 获取指定类指定方法的参数名
	 * 
	 * @param clazz
	 *            要获取参数名的方法所属的类
	 * @param method
	 *            要获取参数名的方法
	 * @return 按参数顺序排列的参数名列表，如果没有参数，则返回null
	 */
	public static String[] getMethodParameterNamesByAsm4(Class<?> clazz, final Method method) {
		final Class<?>[] parameterTypes = method.getParameterTypes();
		if (parameterTypes == null || parameterTypes.length == 0) {
			return null;
		}
		final Type[] types = new Type[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			types[i] = Type.getType(parameterTypes[i]);
		}
		final String[] parameterNames = new String[parameterTypes.length];

		String className = clazz.getName();
		int lastDotIndex = className.lastIndexOf(".");
		className = className.substring(lastDotIndex + 1) + ".class";
		InputStream is = clazz.getResourceAsStream(className);
		try {
			ClassReader classReader = new ClassReader(is);
			classReader.accept(new ClassVisitor(Opcodes.ASM4) {
				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature,
						String[] exceptions) {
					// 只处理指定的方法
					Type[] argumentTypes = Type.getArgumentTypes(desc);
					if (!method.getName().equals(name) || !Arrays.equals(argumentTypes, types)) {
						return null;
					}
					return new MethodVisitor(Opcodes.ASM4) {
						@Override
						public void visitLocalVariable(String name, String desc, String signature, Label start,
								Label end, int index) {
							// 静态方法第一个参数就是方法的参数，如果是实例方法，第一个参数是this
							if (Modifier.isStatic(method.getModifiers())) {
								parameterNames[index] = name;
							} else if (index > 0) {
								parameterNames[index - 1] = name;
							}
						}
					};

				}
			}, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return parameterNames;
	}

}
