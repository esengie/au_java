package ru.spbau.mit;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class LockFreeLazy<T> implements Lazy<T> {
    private Supplier<T> m_supplier;
    private AtomicReference<Box<T>> m_value;

    public LockFreeLazy(Supplier<T> a_supplier) {
        m_supplier = a_supplier;
        m_value = new AtomicReference<>();
    }

    @Override
    public T get() {
        m_value.compareAndSet(null, new Box<>(m_supplier.get()));
        return m_value.get().get();
    }

    class Box<T>{

        private T m_value;

        public Box(T a_value){
            m_value = a_value;
        }

        public T get(){
            return m_value;
        }
    }
}
