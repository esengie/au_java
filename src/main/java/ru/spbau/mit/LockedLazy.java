package ru.spbau.mit;

import java.util.function.Supplier;

public class LockedLazy<T> implements Lazy<T> {
    private Supplier<T> m_supplier;
    private T m_value;

    public LockedLazy(Supplier<T> a_supplier) {
        m_supplier = a_supplier;
    }

    @Override
    public T get() {
        synchronized (this) {
            if (m_supplier != null) {
                m_value = m_supplier.get();
                m_supplier = null;
            }
            return m_value;
        }
    }
}
