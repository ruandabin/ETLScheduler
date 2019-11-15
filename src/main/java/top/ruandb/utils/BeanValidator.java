package top.ruandb.utils;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.apache.commons.collections.MapUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import top.ruandb.exception.ParamException;

/**
 * 全局校验工具类
 * 
 * @author rdb
 *
 */
public class BeanValidator {

	// validator工厂
	private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

	/**
	 * 单个类校验
	 * 
	 * @param t
	 * @param groups
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> Map<String, String> validate(T t, Class... groups) {
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<T>> validateResult = validator.validate(t, groups);
		if (validateResult.isEmpty()) {
			return Collections.emptyMap();
		} else {
			LinkedHashMap errors = Maps.newLinkedHashMap();
			for (ConstraintViolation cv : validateResult) {
				errors.put(cv.getPropertyPath().toString(), cv.getMessage());
			}
			return errors;
		}
	}

	/**
	 * 多个类校验
	 * 
	 * @param collection
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String, String> validateList(Collection<?> collection) {
		Preconditions.checkNotNull(collection);
		Iterator iterator = collection.iterator();
		Map errors;

		do {
			if (!iterator.hasNext()) {
				return Collections.emptyMap();
			}
			Object object = iterator.next();
			errors = validate(object, new Class[0]);
		} while (errors.isEmpty());

		return errors;
	}

	public static Map<String, String> validateObject(Object first, Object... objects) {
		if (objects != null && objects.length > 0) {
			return validateList(Lists.asList(first, objects));
		} else {
			return validate(first, new Class[0]);
		}
	}

	public static void check(Object param) throws ParamException {
		Map<String, String> map = BeanValidator.validateObject(param);
		if (MapUtils.isNotEmpty(map)) {
			throw new ParamException(map.toString());
		}
	}
}
