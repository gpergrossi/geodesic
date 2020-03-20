package com.gpergrossi.util;

import java.util.function.Supplier;

/**
 * A Thread-safe lazy singleton implementation.
 */
public class LazySingleton<T> {

	private Supplier<T> supplier;
	private Object mutex;
	private volatile T instance;
	
	public LazySingleton(Supplier<T> supplier) {
		this.supplier = supplier;
		this.mutex = new Object();
		this.instance = null;
	}
	
	public T getInstance() {
		T result = instance;
		if (result == null) {
			synchronized (mutex) {
				if (instance == null) {
					instance = supplier.get();
					if (instance == null) {
						throw new RuntimeException("Supplier supplied null!");
					}
					else
					{
						// release the supplier because it is no longer needed
						supplier = null;
					}
				}
				result = instance;
			}
		}
		return result;
	}
	
}
