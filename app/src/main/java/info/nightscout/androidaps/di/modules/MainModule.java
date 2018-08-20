package info.nightscout.androidaps.di.modules;

import com.squareup.otto.Bus;
import com.squareup.otto.LoggingBus;
import com.squareup.otto.ThreadEnforcer;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.nightscout.androidaps.Config;
import info.nightscout.androidaps.MainApp;
import info.nightscout.androidaps.interfaces.PluginBase;
import info.nightscout.androidaps.plugins.Actions.ActionsFragment;
import info.nightscout.androidaps.plugins.Careportal.CareportalPlugin;
import info.nightscout.androidaps.plugins.ConfigBuilder.ConfigBuilderPlugin;
import info.nightscout.androidaps.plugins.ConstraintsObjectives.ObjectivesPlugin;
import info.nightscout.androidaps.plugins.ConstraintsSafety.SafetyPlugin;
import info.nightscout.androidaps.plugins.Food.FoodPlugin;
import info.nightscout.androidaps.plugins.Insulin.InsulinOrefFreePeakPlugin;
import info.nightscout.androidaps.plugins.Insulin.InsulinOrefRapidActingPlugin;
import info.nightscout.androidaps.plugins.Insulin.InsulinOrefUltraRapidActingPlugin;
import info.nightscout.androidaps.plugins.IobCobCalculator.IobCobCalculatorPlugin;
import info.nightscout.androidaps.plugins.Loop.LoopPlugin;
import info.nightscout.androidaps.plugins.Maintenance.LoggerUtils;
import info.nightscout.androidaps.plugins.Maintenance.MaintenancePlugin;
import info.nightscout.androidaps.plugins.NSClientInternal.NSClientPlugin;
import info.nightscout.androidaps.plugins.OpenAPSAMA.OpenAPSAMAPlugin;
import info.nightscout.androidaps.plugins.OpenAPSMA.OpenAPSMAPlugin;
import info.nightscout.androidaps.plugins.OpenAPSSMB.OpenAPSSMBPlugin;
import info.nightscout.androidaps.plugins.Overview.OverviewPlugin;
import info.nightscout.androidaps.plugins.Persistentnotification.PersistentNotificationPlugin;
import info.nightscout.androidaps.plugins.ProfileLocal.LocalProfilePlugin;
import info.nightscout.androidaps.plugins.ProfileNS.NSProfilePlugin;
import info.nightscout.androidaps.plugins.ProfileSimple.SimpleProfilePlugin;
import info.nightscout.androidaps.plugins.PumpCombo.ComboPlugin;
import info.nightscout.androidaps.plugins.PumpDanaR.DanaRPlugin;
import info.nightscout.androidaps.plugins.PumpDanaRKorean.DanaRKoreanPlugin;
import info.nightscout.androidaps.plugins.PumpDanaRS.DanaRSPlugin;
import info.nightscout.androidaps.plugins.PumpDanaRv2.DanaRv2Plugin;
import info.nightscout.androidaps.plugins.PumpInsight.InsightPlugin;
import info.nightscout.androidaps.plugins.PumpMDI.MDIPlugin;
import info.nightscout.androidaps.plugins.PumpVirtual.VirtualPumpPlugin;
import info.nightscout.androidaps.plugins.Sensitivity.SensitivityAAPSPlugin;
import info.nightscout.androidaps.plugins.Sensitivity.SensitivityOref0Plugin;
import info.nightscout.androidaps.plugins.Sensitivity.SensitivityOref1Plugin;
import info.nightscout.androidaps.plugins.Sensitivity.SensitivityWeightedAveragePlugin;
import info.nightscout.androidaps.plugins.SmsCommunicator.SmsCommunicatorPlugin;
import info.nightscout.androidaps.plugins.Source.SourceDexcomG5Plugin;
import info.nightscout.androidaps.plugins.Source.SourceGlimpPlugin;
import info.nightscout.androidaps.plugins.Source.SourceMM640gPlugin;
import info.nightscout.androidaps.plugins.Source.SourceNSClientPlugin;
import info.nightscout.androidaps.plugins.Source.SourcePoctechPlugin;
import info.nightscout.androidaps.plugins.Source.SourceXdripPlugin;
import info.nightscout.androidaps.plugins.Treatments.TreatmentsPlugin;
import info.nightscout.androidaps.plugins.Wear.WearPlugin;
import info.nightscout.androidaps.plugins.XDripStatusline.StatuslinePlugin;

@Module
public class MainModule {
    private MainApp mMainApp;

    public MainModule(MainApp mainApp) {
        mMainApp = mainApp;
    }

    @Provides
    @Singleton
    public MainApp provideMainApp() {
        return mMainApp;
    }

    @Provides
    @Singleton
    public Bus provideEventBus() {
        return /*L.isEnabled(L.EVENTS) ? */new LoggingBus(ThreadEnforcer.ANY)/* : new Bus(ThreadEnforcer.ANY)*/;
    }

    @Provides
    @Singleton
    @Named("extFilesDir")
    public String provideExtFileDir() {
        return LoggerUtils.getLogDirectory();
    }

    @Provides
    @Singleton
    @Named("engineeringMode")
    public boolean provideIsEngineeringMode(@Named("extFilesDir") String extFilesDir) {
        File engineeringModeSemaphore = new File(extFilesDir, "engineering_mode");
        return engineeringModeSemaphore.exists() && engineeringModeSemaphore.isFile();
    }

    @Provides
    @Singleton
    public ConfigBuilderPlugin provideConfigBuilder() {
        return ConfigBuilderPlugin.getPlugin();
    }

    @Provides
    @Singleton
    public ArrayList<PluginBase> providePluginsList(MainApp mainApp,
                                                    @Named("engineeringMode") boolean engineeringMode,
                                                    ConfigBuilderPlugin configBuilder) {
        ArrayList<PluginBase> pluginsList = new ArrayList<>();

        // Register all tabs in app here
        pluginsList.add(OverviewPlugin.getPlugin());
        pluginsList.add(IobCobCalculatorPlugin.getPlugin());
        if (Config.ACTION) pluginsList.add(ActionsFragment.getPlugin());
        pluginsList.add(InsulinOrefRapidActingPlugin.getPlugin());
        pluginsList.add(InsulinOrefUltraRapidActingPlugin.getPlugin());
        pluginsList.add(InsulinOrefFreePeakPlugin.getPlugin());
        pluginsList.add(SensitivityOref0Plugin.getPlugin());
        pluginsList.add(SensitivityAAPSPlugin.getPlugin());
        pluginsList.add(SensitivityWeightedAveragePlugin.getPlugin());
        pluginsList.add(SensitivityOref1Plugin.getPlugin());
        if (Config.PUMPDRIVERS) pluginsList.add(DanaRPlugin.getPlugin());
        if (Config.PUMPDRIVERS) pluginsList.add(DanaRKoreanPlugin.getPlugin());
        if (Config.PUMPDRIVERS) pluginsList.add(DanaRv2Plugin.getPlugin());
        if (Config.PUMPDRIVERS) pluginsList.add(DanaRSPlugin.getPlugin());
        pluginsList.add(CareportalPlugin.getPlugin());
        if (Config.PUMPDRIVERS && engineeringMode)
            pluginsList.add(InsightPlugin.getPlugin()); // <-- Enable Insight plugin here
        if (Config.PUMPDRIVERS) pluginsList.add(ComboPlugin.getPlugin());
        if (Config.MDI) pluginsList.add(MDIPlugin.getPlugin());
        pluginsList.add(VirtualPumpPlugin.getPlugin());
        if (Config.APS) pluginsList.add(LoopPlugin.getPlugin());
        if (Config.APS) pluginsList.add(OpenAPSMAPlugin.getPlugin());
        if (Config.APS) pluginsList.add(OpenAPSAMAPlugin.getPlugin());
        if (Config.APS) pluginsList.add(OpenAPSSMBPlugin.getPlugin());
        pluginsList.add(NSProfilePlugin.getPlugin());
        if (Config.OTHERPROFILES) pluginsList.add(SimpleProfilePlugin.getPlugin());
        if (Config.OTHERPROFILES) pluginsList.add(LocalProfilePlugin.getPlugin());
        pluginsList.add(TreatmentsPlugin.getPlugin());
        if (Config.SAFETY) pluginsList.add(SafetyPlugin.getPlugin());
        if (Config.APS) pluginsList.add(ObjectivesPlugin.getPlugin());
        pluginsList.add(SourceXdripPlugin.getPlugin());
        pluginsList.add(SourceNSClientPlugin.getPlugin());
        pluginsList.add(SourceMM640gPlugin.getPlugin());
        pluginsList.add(SourceGlimpPlugin.getPlugin());
        pluginsList.add(SourceDexcomG5Plugin.getPlugin());
        pluginsList.add(SourcePoctechPlugin.getPlugin());
        if (Config.SMSCOMMUNICATORENABLED) pluginsList.add(SmsCommunicatorPlugin.getPlugin());
        pluginsList.add(FoodPlugin.getPlugin());

        pluginsList.add(WearPlugin.initPlugin(mainApp));
        pluginsList.add(StatuslinePlugin.initPlugin(mainApp));
        pluginsList.add(PersistentNotificationPlugin.getPlugin());
        pluginsList.add(NSClientPlugin.getPlugin());
        pluginsList.add(MaintenancePlugin.initPlugin(mainApp));

        pluginsList.add(configBuilder);

        return pluginsList;
    }
}
