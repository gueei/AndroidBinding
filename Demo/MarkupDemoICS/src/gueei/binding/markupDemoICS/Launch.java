package gueei.binding.markupDemoICS;

import gueei.binding.markupDemoICS.viewModels.LaunchViewModel;
import gueei.binding.v30.app.BindingActivityV30;
import android.os.Bundle;

public class Launch extends BindingActivityV30 {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LaunchViewModel vm = new LaunchViewModel(this);
        this.inflateAndBind(R.xml.main_metadata, vm);
    }
}