package com.sql.mvp.testapp.utils.pagination;

import rx.Observable;

public interface PagingListener<T> {
    Observable<T> onNextPage(int offset);
}
