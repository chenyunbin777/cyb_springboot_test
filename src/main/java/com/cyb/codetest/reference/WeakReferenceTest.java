package com.cyb.codetest.reference;

import java.lang.ref.WeakReference;
import java.util.Date;

/**
 * @author cyb
 * @date 2022/3/10 下午3:07
 */
public class WeakReferenceTest {

    WeakReference weakReference = new WeakReference<Object>(new Date());
}
