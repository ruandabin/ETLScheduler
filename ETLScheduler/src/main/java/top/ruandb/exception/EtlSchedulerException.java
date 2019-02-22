package top.ruandb.exception;

/**
 * 全局异常
 * @author rdb
 *
 */
@SuppressWarnings("serial")
public class EtlSchedulerException extends RuntimeException{

	public EtlSchedulerException() {
		super();
	}

	public EtlSchedulerException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EtlSchedulerException(String message, Throwable cause) {
		super(message, cause);
	}

	public EtlSchedulerException(String message) {
		super(message);
	}

	public EtlSchedulerException(Throwable cause) {
		super(cause);
	}

	
}
