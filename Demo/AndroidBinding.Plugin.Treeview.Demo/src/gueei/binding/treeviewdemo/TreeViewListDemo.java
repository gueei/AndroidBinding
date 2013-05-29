package gueei.binding.treeviewdemo;

import gueei.binding.app.BindingActivity;
import gueei.binding.treeviewdemo.viewModels.TreeViewListViewModel;
import android.os.Bundle;

public class TreeViewListDemo extends BindingActivity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TreeViewListViewModel vm = new TreeViewListViewModel(this);
        this.setAndBindRootView(R.layout.main, vm);
        this.setAndBindOptionsMenu(R.menu.main_menu, vm);
    }
}