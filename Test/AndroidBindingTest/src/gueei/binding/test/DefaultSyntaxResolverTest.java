package gueei.binding.test;

import gueei.binding.IObservable;
import gueei.binding.ISyntaxResolver;
import gueei.binding.ISyntaxResolver.SyntaxResolveException;
import gueei.binding.kernel.DefaultSyntaxResolver;
import android.test.AndroidTestCase;

public class DefaultSyntaxResolverTest extends AndroidTestCase {
	final int pass = 10;
	ISyntaxResolver resolver = new DefaultSyntaxResolver();
	
	public void testResolvePositiveInteger() throws SyntaxResolveException{
		IObservable<?> obs;
		for(int i=0; i<pass; i++){
			Integer testValue = i * 131;
			obs = resolver.constructObservableFromStatement(getContext(), testValue.toString(), this);
			assertEquals(testValue, obs.get());			
		}
	}
	
	public void testResolveNegativeInteger() throws SyntaxResolveException{
		IObservable<?> obs;
		for(int i=0; i<pass; i++){
			Integer testValue = i * -131;
			obs = resolver.constructObservableFromStatement(getContext(), testValue.toString(), this);
			assertEquals(testValue, obs.get());			
		}
	}

	public void testResolvePositiveFloat() throws SyntaxResolveException{
		IObservable<?> obs;
		for(int i=1; i<pass; i++){
			Float testValue = i * 1.321f;
			obs = resolver.constructObservableFromStatement(getContext(), testValue.toString(), this);
			assertEquals(testValue, obs.get());
		}
	}

	public void testResolveNegativeFloat() throws SyntaxResolveException{
		IObservable<?> obs;
		for(int i=1; i<pass; i++){
			Float testValue = i * -3.5121f;
			obs = resolver.constructObservableFromStatement(getContext(), testValue.toString(), this);
			assertEquals(testValue, obs.get());
		}
	}
}
