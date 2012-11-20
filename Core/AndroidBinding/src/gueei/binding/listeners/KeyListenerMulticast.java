package gueei.binding.listeners;

import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class KeyListenerMulticast extends ViewMulticastListener<KeyListener>
	implements KeyListener{

	private KeyListener originalListener;
	
	@Override
	public void registerToView(View v) {
		if (!(v instanceof TextView)) return;
		originalListener = ((TextView)v).getKeyListener();
		((TextView)v).setKeyListener(this);
	}

	public void clearMetaKeyState(View arg0, Editable arg1, int arg2) {
		if (originalListener!=null)
			originalListener.clearMetaKeyState(arg0, arg1, arg2);
	}

	public int getInputType() {
		if (originalListener!=null)
			return originalListener.getInputType();
		return 0;
	}

	public boolean onKeyDown(View arg0, Editable arg1, int arg2, KeyEvent arg3) {
		if (originalListener!=null)
			return originalListener.onKeyDown(arg0, arg1, arg2, arg3);
		return false;
	}

	public boolean onKeyOther(View arg0, Editable arg1, KeyEvent arg2) {
		if (originalListener!=null)
			return originalListener.onKeyOther(arg0, arg1, arg2);
		return false;
	}

	public boolean onKeyUp(View arg0, Editable arg1, int arg2, KeyEvent arg3) {
		if (originalListener!=null)
			return originalListener.onKeyUp(arg0, arg1, arg2, arg3);
		
		return false;
	}
	
}
