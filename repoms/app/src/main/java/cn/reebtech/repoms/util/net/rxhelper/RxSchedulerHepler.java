package cn.reebtech.repoms.util.net.rxhelper;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by on 2017/6/3 08:45 *
 * 作用: RxJava  的线程管理ObservableTransformer
 */

public class RxSchedulerHepler {

    public static <T> ObservableTransformer<T,T> io_main(){
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
               return  upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
