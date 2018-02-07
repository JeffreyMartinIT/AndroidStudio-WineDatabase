package edu.kvcc.cis298.cis298assignment4;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by Jeffrey Martin.
 */
public class BeverageListFragment extends Fragment {

    //Private variables for the recycler view and the required adapter
    private RecyclerView mBeverageRecyclerView;
    private BeverageAdapter mBeverageAdapter;
    private static final String TAG = "Beverage";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "about to  BeverageFetcher().fetchBeverages()");
        new FetchBeveragesTask().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Use the inflator to create a new view
        View view = inflater.inflate(R.layout.fragment_beverage_list, container, false);

        //Get a handle for the recycler view
        mBeverageRecyclerView = (RecyclerView) view.findViewById(R.id.beverage_recycler_view);

        //Set the layout manager, which is a required piece of the view
        mBeverageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Call the updateUI method to do any remaining setup and get the view displayed
        updateUI();

        //return the view
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //If this fragment is resumed from returning from the detail view, update the UI
        updateUI();
    }

    //Method to setup the view with an adapter if it doesn't already have one.
    //and update changes if it does.
    private void updateUI() {
        //Get the collection of data.
        BeverageCollection beverageCollection = BeverageCollection.get(getActivity());
        //Fetch the list of data from the collection
        List<Beverage> beverages = beverageCollection.getBeverages();

        //If there is no adapter, make a new one and send it in the list of beverages
        if (mBeverageAdapter == null) {
           /* mBeverageAdapter = new BeverageAdapter(beverages);
            //set the adapter for the recyclerview to the newly created adapter
            mBeverageRecyclerView.setAdapter(mBeverageAdapter);
            */

            setupAdapter();
        } else {
            //adapter already exists, so just call the notify data set changed method to update
            mBeverageAdapter.notifyDataSetChanged();
        }
    }

    // Method to set up the adapter for the recycler view
    private void setupAdapter(){
        //Check to see if the gragment has been added. In other words is the thread done.
        if (isAdded()){
            //Get a reference to the BeverageColection
            BeverageCollection beverageCollection = BeverageCollection.get(getActivity());
            //Create a new adapter to the beverage list
            mBeverageAdapter = new BeverageAdapter(beverageCollection.getBeverages());
            //Set the adapter on the recycler view.
            mBeverageRecyclerView.setAdapter(mBeverageAdapter);
        }
    }
    //Private class that is required to get a recyclerview working
    private class BeverageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //private widget vars
        private TextView mNameTextView;
        private TextView mPriceTextView;
        private TextView mIdTextView;

        //private var for a single beverage
        private Beverage mBeverage;

        //constructor that takes in a view
        public BeverageHolder(View itemView) {
            super(itemView);
            //Set an onclick listener for the view holder
            itemView.setOnClickListener(this);

            //Wire up the vars to the layout widgets
            mNameTextView = (TextView) itemView.findViewById(R.id.list_item_name);
            mIdTextView = (TextView) itemView.findViewById(R.id.list_item_id);
            mPriceTextView = (TextView) itemView.findViewById(R.id.list_item_price);
        }

        //Implement the bindBeverage method that takes in a single beverage
        public void bindBeverage(Beverage beverage) {
            //set the beverage to the class level one
            mBeverage = beverage;

            //set the name and id from the model
            mNameTextView.setText(mBeverage.getName());
            mIdTextView.setText(mBeverage.getId());

            //Create and use a number formatter that will display the price as currency
            NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();
            mPriceTextView.setText(moneyFormat.format(mBeverage.getPrice()));
        }

        //Override the onClick method so clicking on a viewHolder starts up a new activity
        @Override
        public void onClick(View v) {
            //get a properly formatted intent from the BeveragePagerActivity
            Intent intent = BeveragePagerActivity.newIntent(getActivity(), mBeverage.getId());
            //Start the new activity
            startActivity(intent);
        }
    }

    //Private class for the adapter that the recyclerview needs
    private class BeverageAdapter extends RecyclerView.Adapter<BeverageHolder> {
        //private list of the beverages
        private List<Beverage> mBeverages;

        //Constructor that takes in a list of beverages and then assigns it to the class level version
        public BeverageAdapter(List<Beverage> beverages) {
            mBeverages = beverages;
        }

        //Method to get the create the view holder
        @Override
        public BeverageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //get an inflator
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            //inflate the view
            View view = layoutInflater.inflate(R.layout.list_item_beverage, parent, false);
            //return a new holder with the view
            return new BeverageHolder(view);
        }

        //Method to bind a view holder to a beverage
        @Override
        public void onBindViewHolder(BeverageHolder beverageHolder, int position) {
            //get the beverage from the list based on the position being searched for
            Beverage beverage = mBeverages.get(position);
            //call the bind beverage method with the found beverage
            beverageHolder.bindBeverage(beverage);
        }

        //Method to get the size of the list of beverages.
        @Override
        public int getItemCount() {
            return mBeverages.size();
        }
    }

    //Private Class to do the networking in a separate thread
    private class FetchBeveragesTask extends AsyncTask<Void,View, List<Beverage>>{
        @Override
        protected List<Beverage> doInBackground(Void... params) {
            Log.d(TAG, "about to return new BeverageFetcher().fetchBeverages();");
            //Create a new BeverageFetcher class and call the fetchBeverages method.
            return new BeverageFetcher().fetchBeverages();
        }

        @Override
        //Method that will automaticlly be called when the code doInBackground is done.
        protected void onPostExecute(List<Beverage> beverages) {
            //Get a reference to the BeverageCollection
            BeverageCollection beverageCollection = BeverageCollection.get(getActivity());
            //Use the setter setBevaragecollection to set the beverage to the passed beverages
            beverageCollection.setBeverageCollection(beverages);
            //Set up the adapter for the recycler view from the data source.
            setupAdapter();
        }
    }
}