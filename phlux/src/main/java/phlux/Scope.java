package phlux;

import android.os.Bundle;

import java.util.UUID;

/**
 * Represents an easy and type-safe access to {@link Phlux}.
 * <p>
 * {@link Scope} is an interface to access internal state of a View,
 * including its background tasks and callbacks.
 */
public class Scope<S extends ViewState> {

    private final String key;
    private final Phlux phlux = Phlux.INSTANCE;

    /**
     * Constructs a new scope from a given initial state.
     */
    public Scope(S state) {
        this.key = UUID.randomUUID().toString();
        phlux.create(key, state);
    }

    /**
     * Restores a scope from a given {@link Bundle}.
     */
    public Scope(Bundle bundle) {
        this.key = bundle.getString("key");
        phlux.restore(key, bundle.getParcelable("scope"));
    }

    /**
     * Returns the current scope's state.
     */
    public S state() {
        return (S) Phlux.INSTANCE.state(key);
    }

    /**
     * Saves the scope into {@link Bundle}.
     */
    public Bundle save() {
        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        bundle.putParcelable("scope", Phlux.INSTANCE.get(key));
        return bundle;
    }

    /**
     * Applies a function to the scope's state.
     */
    public ApplyResult<S> apply(Function<S> function) {
        return phlux.apply(key, function);
    }

    /**
     * Executes a background background.
     * <p>
     * Sticky means that the request will not be removed after it's execution and will be re-executed
     * on a process termination.
     */
    public void background(int id, Background<S> background) {
        phlux.background(key, id, background);
    }

    /**
     * Drops a background task.
     * The task will be executed without interruption as usual, but it's application function will not be called.
     */
    public void drop(int id) {
        phlux.drop(key, id);
    }

    /**
     * Finally dispose the scope. Do this on Activity.onDestroy when isFinishing() is true.
     * If the scope is controlled by a Fragment then you need to manually control it's existence.
     */
    public void remove() {
        phlux.remove(key);
    }

    /**
     * Registers a callback for state updates.
     * Once registered the callback will be fired immediately.
     */
    public void register(StateCallback<S> callback) {
        phlux.register(key, callback);
    }

    /**
     * Unregisters a given state callback.
     */
    public void unregister(StateCallback<S> callback) {
        phlux.unregister(key, callback);
    }
}
