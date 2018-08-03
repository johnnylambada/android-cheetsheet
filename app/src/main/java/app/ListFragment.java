package app;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.databinding.FragmentListBinding;

public class ListFragment extends Fragment {

    private FragmentListBinding binder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        binder.getRoot().setOnClickListener(__->
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container,new DetailFragment())
                        .addToBackStack(null)
                        .commit()
        );
        return binder.getRoot();
    }
}
