/**
 * Project Name:midai-pay-common
 * File Name:KryoRedisSerializer.java
 * Package Name:com.midai.framework.config.redis
 * Date:2016年9月4日上午10:56:05
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.framework.config.redis;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * ClassName:KryoRedisSerializer <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月4日 上午10:56:05 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public class KryoRedisSerializer<T> implements RedisSerializer<T> {
    
    private final Kryo kryo = new Kryo();  
    
    @Override  
    public byte[] serialize(Object t) throws SerializationException {  
        byte[] buffer = new byte[2048];  
        Output output = new Output(buffer);  
        kryo.writeClassAndObject(output, t);  
        return output.toBytes();  
    }  
  
    @Override  
    public T deserialize(byte[] bytes) throws SerializationException {  
        Input input = new Input(bytes);  
        @SuppressWarnings("unchecked")  
        T t = (T) kryo.readClassAndObject(input);  
        return t;  
    }  

}

