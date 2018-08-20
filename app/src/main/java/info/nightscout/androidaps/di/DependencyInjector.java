package info.nightscout.androidaps.di;

import info.nightscout.androidaps.MainApp;
import info.nightscout.androidaps.di.components.DaggerMainComponent;
import info.nightscout.androidaps.di.components.MainComponent;
import info.nightscout.androidaps.di.modules.MainModule;

public class DependencyInjector {
    private MainComponent mMainComponent;

    public DependencyInjector(MainApp mainApp) {
        mMainComponent = DaggerMainComponent
                .builder()
                .mainModule(new MainModule(mainApp))
                .build();
    }

    public MainComponent getMainComponent() {
        return mMainComponent;
    }
}
