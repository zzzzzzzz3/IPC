package com.example.msi.ipc.modle;

import android.os.RemoteException;

import com.example.msi.ipc.ISecurityCenter;

/**
 * 文 件 名: SecurityCenterImpl
 * 创 建 人: ZhangRonghua
 * 创建日期: 2017/1/19 00:06
 * 邮   箱: qq798435167@gmail.com
 * 博   客: http://zzzzzzzz3.github.io
 * 修改时间：
 * 修改备注：
 */
public class SecurityCenterImpl extends ISecurityCenter.Stub {
    private static final char CODE = '^';

    @Override
    public String encrypt(String content) throws RemoteException {
        char[] chars = content.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] ^= CODE;
        }
        return new String(chars);
    }

    @Override
    public String decrypt(String password) throws RemoteException {
        return encrypt(password);
    }
}
