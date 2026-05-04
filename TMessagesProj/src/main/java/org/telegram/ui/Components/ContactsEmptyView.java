/*
 * This is the source code of Telegram for Android v. 7.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2020.
 */

package org.telegram.ui.Components;

import static org.telegram.messenger.AndroidUtilities.dp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Stories.recorder.ButtonWithCounterView;

public class ContactsEmptyView extends LinearLayout {

    private final TextView titleTextView;
    private final TextView subtitleTextView;
    private final ButtonWithCounterView button;

    private final int currentAccount = UserConfig.selectedAccount;

    public ContactsEmptyView(Context context) {
        super(context);

        setOrientation(LinearLayout.VERTICAL);

        String lang = LocaleController.getInstance().getCurrentLocaleInfo().shortName;
        boolean ru = lang != null && lang.toLowerCase().startsWith("ru");

        titleTextView = new TextView(context);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        titleTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        titleTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        titleTextView.setTypeface(AndroidUtilities.bold());
        titleTextView.setText(ru ? "Добавить контакты" : "Add contacts");
        addView(titleTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL, 0, 15, 0, 7));

        subtitleTextView = new TextView(context);
        subtitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        subtitleTextView.setTextColor(Theme.getColor(Theme.key_emptyListPlaceholder));
        subtitleTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        subtitleTextView.setText(ru ? "У вас пока нет контактов — вы можете их добавить." : "You don’t have any contacts yet — you can add them.");
        addView(subtitleTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL, 0, 0, 0, 19));

        button = new ButtonWithCounterView(context, null);
        button.setUseWrapContent(true);
        button.setRound();

        SpannableStringBuilder ssb = new SpannableStringBuilder("c");
        ssb.setSpan(new ColoredImageSpan(R.drawable.filled_new_contact_24), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append("  ").append(ru ? "Новый контакт" : "New Contact");

        button.setText(ssb, false);
        addView(button, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, 44, Gravity.CENTER_HORIZONTAL));
    }

    protected void onInviteClick() {
        Activity activity = AndroidUtilities.findActivity(getContext());
        if (activity == null || activity.isFinishing()) return;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String text = ContactsController.getInstance(currentAccount).getInviteText(0);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        activity.startActivity(Intent.createChooser(intent, text));
    }

    public void setColors() {
        titleTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        subtitleTextView.setTextColor(Theme.getColor(Theme.key_emptyListPlaceholder));
        button.updateColors();
    }
}