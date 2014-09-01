package com.minibittechnologies;

import java.util.ArrayList;
import org.woodyguthriecenter.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.minibittechnologies.adapter.CustomAdapterForOfferDetails;
import com.minibittechnologies.model.Post;

public class MainActivity extends Activity {
	ArrayList<Post> list;
	ListView lv_offer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		list = new ArrayList<>();
		// loadarrlist();
		lv_offer = (ListView) findViewById(R.id.lv_offer);
		CustomAdapterForOfferDetails adapter = new CustomAdapterForOfferDetails(MainActivity.this, list);
		lv_offer.setAdapter(adapter);

	}

	// public void loadarrlist(){
	// String
	// message="Iphone 5S Camera Samples Untouched IPhone 5s Camera Samples Photos Redmond Pie,"
	// +
	// " download this wallpaper for free in HD resolution. Iphone 5S Camera Samples Untouched IPhone 5s"
	// +
	// " Camera Samples Photos Redmond Pie was posted in November 26, 2013 at 1:18 pm. This HD Wallpaper Iphone "
	// +
	// "5S Camera Samples Untouched IPhone 5s Camera Samples Photos Redmond Pie has viewed by 710 users. If you wanna have it"
	// +
	// " as yours, please click the wallpaper and you will go to page download, so you just choose the size above the wallpaper that"
	// +
	// " you want in Download Size, Click it and download the " +
	// "Iphone 5S Camera Samples Untouched IPhone 5s Camera Samples Photos Redmond Pie wallpaper.";
	// String
	// url="http://www.lipberry.com//uploaded_content//article//1406.jpg";
	//
	// Post offerDetails=new Post("Iphone 5S Camera Samples Untouched I",
	// message, url, false);
	// list.add(offerDetails);
	//
	//
	//
	// message="In all our business operations, we strive to play an integral role in society, sharing its values and working to create new value to help people around the world have healthier and more fulfilling lives."+
	//
	// "This is the essence of the Olympus management philosophy of Social IN. As a member of society, Olympus places the utmost importance on its relationships with individuals. Accordingly, Olympus has adopted Social IN, a management philosophy that embraces social values as an essential part of its business ideology."+
	//
	// "Olympus was founded with the declared purpose of manufacturing microscopes that would garner recognition in the global market. The impetus for this drive was a deep sense of duty to help advance medical development through the domestic manufacture of microscopes."+
	//
	// "Since then, Olympus confirmed its position as a camera manufacturer and developed gastrocameras. Following this achievement, Olympus made a number of dreams come true through the application of Opto-Digital Technology, an area of core competence that integrates longstanding optical technology and state-of-the-art digital technology."+
	//
	// "Your Vision, Our Future embodies our determination to realize dreams in a close bond with society while coexisting in harmony. Based on this corporate slogan, Olympus is pursuing innovation in order to become a company where all stakeholders, including shareholders, clients and employees, are proud of the Olympus brand.";
	// url="http://www.lipberry.com//uploaded_content//article//1405.jpg";
	// offerDetails=new Post("E-620: Sample Images | Digital SLR |", message,
	// url, false);
	// list.add(offerDetails);
	//
	// message="Beginning in 1934 as Japan's pioneering photographic film maker, Fujifilm has leveraged its imaging and information technology to become a global presence known for innovation in healthcare, graphic arts, optical devices, highly functional materials and other high-tech areas. Beginning in 1934 as Japan's pioneering photographic film maker, Fujifilm has leveraged its imaging and information technology to become a global presence known for innovation in healthcare, graphic arts, optical devices, highly functional materials and other high-tech areas.Beginning in 1934 as Japan's pioneering photographic film maker, Fujifilm has leveraged its imaging and information technology to become a global presence known for innovation in healthcare, graphic arts, optical devices, highly functional materials and other high-tech areas.";
	// url="http://www.lipberry.com//uploaded_content//article//1402.jpg";
	// offerDetails=new Post("FUJIFILM X-S1 | Sample Images | Fujifilm",
	// message, url, false);
	// list.add(offerDetails);
	// }
}
