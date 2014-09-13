/**
 * 
 */
package com.devotify.gabrielhorn.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devotify.gabrielhorn.R;

/**
 * @author Touhid
 * 
 */
public class FragmentPrivacyPolicy extends Fragment {

	private final String TAG = this.getClass().getSimpleName();

	public static Fragment newInstance() {
		return new FragmentPrivacyPolicy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "inside onCreateView");
		View v = inflater.inflate(R.layout.frag_privacy_terms, container, false);
		// TODO set privacy policy text
		TextView tv = (TextView) v.findViewById(R.id.tvPrivacyOrTerms);
		// tv.setText(Html.fromHtml(getActivity().getResources().getString(R.string.privacy_policy)));
		SpannableString sp = new SpannableString(
				"PRIVACY POLICY\n\n"
						+ "This privacy policy governs your use of the software application Woody Guthrie (“Application”) for mobile devices that was created by Devotify LLC. The Application is used to uniquely connect users to Woody Guthrie. Users can stay up to date with Woody Guthrie’s latest specials and use the easy to use rewards card every time they visit his office. Users can also redeem posted offers simply by showing Woody Guthrie their app. Sharing deals on Facebook and Twitter is also an added feature.\n"
						+ "\nWhat information does the Application obtain and how is it used?\n"
						+ "\nUser Provided Information\n"
						+ "Registration with us is optional. However, please keep in mind that you may not be able to use some of the features offered by the Application unless you register with us including the rewards card.\n"
						+ "\nWhen you register with us and use the rewards card section of our Application, you generally provide (a) your name, email address, password and other registration information.\n"
						+ "We may also use the information you provided us to contact you from time to time to provide you with important information, required notices and marketing promotions.\n"
						+ "\nAutomatically Collected Information\n\n"
						+ "In addition, the Application may collect certain information automatically, including, but not limited to, the type of mobile device you use, your mobile devices unique device ID, the IP address of your mobile device, your mobile operating system, the type of mobile Internet browsers you use, and information about the way you use the Application.\n"
						+ "\nDoes the Application collect precise real time location information of the device?\n"
						+ "\nThis Application does not collect precise information about the location of your mobile device.\n"
						+ "\nDo third parties see and/or have access to information obtained by the Application?\n"
						+ "\nOnly aggregated, anonymized data is periodically transmitted to external services to help us improve the Application and our service. We will share your information with third parties only in the ways that are described in this privacy statement.\n"
						+ "We may disclose User Provided and Automatically Collected Information:\n"
						+ "		●  as required by law, such as to comply with a subpoena, or similar legal process; \n"
						+ "		●  when we believe in good faith that disclosure is necessary to protect our rights, protect "
						+ "your safety or the safety of others, investigate fraud, or respond to a government "
						+ "request; \n"
						+ "		●  with our trusted services providers who work on our behalf, do not have an \n"
						+ "independent use of the information we disclose to them, and have agreed to adhere to "
						+ "the rules set forth in this privacy statement. \n"
						+ "		●  if Devotify LLC is involved in a merger, acquisition, or sale of all or a portion of its "
						+ "assets, you will be notified via email and/or a prominent notice on our web site of any change in ownership or uses of this information, as well as any choices you may have regarding this information. \n"
						+ "\nWhat are my opt­out rights? \n"
						+ "\nYou can stop all collection of information by the Application easily by uninstalling the Application. You may use the standard uninstall processes as may be available as part of your mobile device or via the mobile application marketplace or network. You can also request to opt-out via email, at privacy@devotify.com. \n"
						+ "\nData Retention Policy, Managing Your Information \n"
						+ "\nWe will retain User Provided data for as long as you use the Application and for a reasonable time thereafter. We will retain Automatically Collected information for up to 24 months and thereafter may store it in aggregate. If you’d like us to delete User Provided Data that you have provided via the Application, please contact us at privacy@devotify.com and we will respond in a reasonable time. Please note that some or all of the User Provided Data may be required in order for the Application to function properly. \n"
						+ "  \n"
						+ "\nChildren\n"
						+ "\nWe do not use the Application to knowingly solicit data from or market to children under the age of 13. If a parent or guardian becomes aware that his or her child has provided us with information without their consent, he or she should contact us at privacy@devotify.com. We will delete such information from our files within a reasonable time.\n"
						+ "\nSecurity\n"
						+ "\nWe are concerned about safeguarding the confidentiality of your information. We provide physical, electronic, and procedural safeguards to protect information we process and maintain. For example, we limit access to this information to authorized employees and contractors who need to know that information in order to operate, develop or improve our Application. Please be aware that, although we endeavor provide reasonable security for information we process and maintain, no security system can prevent all potential security breaches.\n"
						+ "\nChanges\n"
						+ "\nThis Privacy Policy may be updated from time to time for any reason. We will notify you of any changes to our Privacy Policy by posting the new Privacy Policy here and informing you via email or text message. You are advised to consult this Privacy Policy regularly for any changes, as continued use is deemed approval of all changes. You can check the history of this policy by devotify.com/privacy.\n"
						+ "\nYour Consent\n"
						+ "\nBy using the Application, you are consenting to our processing of your information as set forth in this Privacy Policy now and as amended by us. \"Processing,\" means using cookies on a computer/hand held device or using or touching information in any way, including, but not limited to, collecting, storing, deleting, using, combining and disclosing information, all of which activities will take place in the United States. If you reside outside the United States your information will be transferred, processed and stored there under United States privacy standards.\n\n"
						+ "\n"
						+ "Contact us\n"
						+ "If you have any questions regarding privacy while using the Application, or have questions about our practices, please contact us via email at privacy@devotify.com.");
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 14,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 507, 601,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setText(sp);
		return v;
	}

}
