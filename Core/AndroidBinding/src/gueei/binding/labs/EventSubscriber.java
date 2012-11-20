package gueei.binding.labs;

import android.os.Bundle;

public interface EventSubscriber {
	public void onEventTriggered(final String eventName, final Object publisher, final Bundle data);
}
