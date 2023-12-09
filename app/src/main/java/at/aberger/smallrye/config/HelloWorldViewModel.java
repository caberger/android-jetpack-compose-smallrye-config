package at.aberger.smallrye.config;

import androidx.lifecycle.ViewModel;

import java.util.function.Consumer;

import at.aberger.util.Mapper;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class HelloWorldViewModel extends ViewModel {
    Mapper<HelloWorldModel> mapper = new Mapper<>(HelloWorldModel.class);
    public final BehaviorSubject<HelloWorldModel> store = BehaviorSubject.createDefault(new HelloWorldModel());

    public void next(Consumer<HelloWorldModel> reducer) {
        var current = store.getValue();
        var nextState = mapper.clone(current);
        reducer.accept(nextState);
        store.onNext(nextState);
    }
}