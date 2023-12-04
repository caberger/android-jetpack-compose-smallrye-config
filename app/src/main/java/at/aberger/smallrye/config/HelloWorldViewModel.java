package at.aberger.smallrye.config;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class HelloWorldViewModel extends ViewModel {
    public final BehaviorSubject<HelloWorldModel> model = BehaviorSubject.createDefault(new HelloWorldModel());
}