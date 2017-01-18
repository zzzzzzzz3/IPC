package com.example.msi.ipc.modle;

import android.os.IBinder;
import android.os.RemoteException;

import com.example.msi.ipc.IBinderPool;

/**
 * 文 件 名: BinderPoolImpl
 * 创 建 人: ZhangRonghua
 * 创建日期: 2017/1/19 00:10
 * 邮   箱: qq798435167@gmail.com
 * 博   客: http://zzzzzzzz3.github.io
 * 修改时间：
 * 修改备注：
 */
public class BinderPoolImpl extends IBinderPool.Stub {
    public static final int SECURITY_CODE = 0;

    @Override
    public IBinder queryBinder(int binderCode) throws RemoteException {
        IBinder binder = null;
        switch (binderCode){
            case SECURITY_CODE:{
                binder = new SecurityCenterImpl();
                break;
            }
        }
        return binder;
    }
}
