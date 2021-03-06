package com.jd.blockchain.crypto.serialize;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.crypto.SignatureDigest;
import com.jd.blockchain.utils.Bytes;
import com.jd.blockchain.utils.codec.Base58Utils;
import com.jd.blockchain.utils.io.BytesSlice;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Map;

public class ByteArrayObjectDeserializer extends JavaBeanDeserializer {

    private ByteArrayObjectDeserializer(Class<?> clazz) {
        super(ParserConfig.global, clazz);
    }

	public static ByteArrayObjectDeserializer getInstance(Class<?> clazz) {
		return new ByteArrayObjectDeserializer(clazz);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
		if (type instanceof Class && clazz.isAssignableFrom((Class<?>) type)) {
			String base58Str = parser.parseObject(String.class);
			byte[] hashBytes = Base58Utils.decode(base58Str);
			if (clazz == HashDigest.class) {
				return (T) new HashDigest(hashBytes);
			} else if (clazz == PubKey.class) {
				return (T) new HashDigest(hashBytes);
			} else if (clazz == SignatureDigest.class) {
				return (T) new SignatureDigest(hashBytes);
			} else if (clazz == Bytes.class) {
				return (T) new Bytes(hashBytes);
			} else if (clazz == BytesSlice.class) {
				return (T) new BytesSlice(hashBytes);
			}
		}
		return (T) parser.parse(fieldName);
	}

	@Override
	public Object createInstance(Map<String, Object> map, ParserConfig config) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (map == null || map.isEmpty()) {
            return null;
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof String) {
				byte[] hashBytes = Base58Utils.decode((String)value);
				if (clazz == HashDigest.class) {
					return new HashDigest(hashBytes);
				} else if (clazz == PubKey.class) {
					return new PubKey(hashBytes);
				} else if (clazz == SignatureDigest.class) {
					return new SignatureDigest(hashBytes);
				} else if (clazz == Bytes.class) {
					return new Bytes(hashBytes);
				} else if (clazz == BytesSlice.class) {
					return new BytesSlice(hashBytes);
				}
            }
        }
        return null;
	}

	@Override
	public int getFastMatchToken() {
		return JSONToken.LBRACE;
	}
}
