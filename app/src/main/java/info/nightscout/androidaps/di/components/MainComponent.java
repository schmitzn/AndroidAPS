package info.nightscout.androidaps.di.components;

import javax.inject.Singleton;

import dagger.Component;
import info.nightscout.androidaps.MainActivity;
import info.nightscout.androidaps.di.modules.MainModule;

@Singleton
@Component(modules = {MainModule.class})
public interface MainComponent {

    void inject(MainActivity mainActivity);
}
