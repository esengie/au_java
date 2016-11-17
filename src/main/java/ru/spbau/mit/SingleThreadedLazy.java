package ru.spbau.mit;

import java.util.function.Supplier;

public class SingleThreadedLazy<T> implements Lazy<T> {
    private Supplier<T> m_supplier;
    private T m_value;

    public SingleThreadedLazy(Supplier<T> a_supplier) {
        m_supplier = a_supplier;
    }

    @Override
    public T get() {
        if (m_supplier != null) {
            m_value = m_supplier.get();
            m_supplier = null;
        }
        return m_value;
    }

}
