package com.iocm.business.utils.album;

/**
 * Created by Jack on 2015/10/20.
 */
public interface AlbumCallback<T> {

    /**
     * @param object
     */
    void onCompleted(T object);

    /**
     * @param str
     */
    void onFailed(String str);
}
