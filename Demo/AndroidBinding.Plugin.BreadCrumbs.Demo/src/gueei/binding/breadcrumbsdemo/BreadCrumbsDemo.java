package gueei.binding.breadcrumbsdemo;

import gueei.binding.breadcrumbsdemo.viewModels.BreadCrumbsViewModel;
import gueei.binding.v30.app.BindingActivityV30;
import android.os.Bundle;

public class BreadCrumbsDemo extends BindingActivityV30 {
	@SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BreadCrumbsViewModel vm = new BreadCrumbsViewModel(this);
        this.setAndBindRootView(R.layout.main, vm);
        this.setAndBindOptionsMenu(R.menu.main_menu, vm);
    }
}