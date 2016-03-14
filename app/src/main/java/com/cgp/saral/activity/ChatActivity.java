    package com.cgp.saral.activity;


    import android.app.Activity;
    import android.app.ProgressDialog;
    import android.content.Intent;
    import android.os.Bundle;
    import android.support.annotation.Nullable;
    import android.support.v4.app.Fragment;
    import android.support.v4.app.FragmentManager;
    import android.support.v4.app.FragmentTransaction;
    import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.Toolbar;
    import android.view.View;
    import android.widget.TextView;

    import com.cgp.saral.R;
    import com.cgp.saral.myutils.Constants;
    import com.zopim.android.sdk.api.Chat;
    import com.zopim.android.sdk.api.ZopimChat;
    import com.zopim.android.sdk.chatlog.ZopimChatLogFragment;
    import com.zopim.android.sdk.embeddable.ChatActions;
    import com.zopim.android.sdk.model.VisitorInfo;
    import com.zopim.android.sdk.prechat.ChatListener;
    import com.zopim.android.sdk.prechat.ZopimChatFragment;
    import com.zopim.android.sdk.widget.ChatWidgetService;


    /**
     * A simple {@link Fragment} subclass.
     */
    public class ChatActivity extends AppCompatActivity implements ChatListener {


        private static final String ARG_PAGE_NUMBER = "page_number";

        private View view;

        Toolbar toolbar;
        public ChatActivity() {
            // Required empty public constructor
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_chat);
            ((TextView)findViewById(R.id.txt_title)).setText("Chat");

            toolbar = (Toolbar) findViewById(R.id.toolbar);


            if(toolbar != null) {
                setSupportActionBar(toolbar);
                // getSupportActionBar().setTitle("SaralApp");
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            }
            findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            /**
             * If starting activity while the chat widget is actively presented the activity will resume the current chat
             */
            boolean widgetWasActive = stopService(new Intent(this, ChatWidgetService.class));
            if (widgetWasActive) {
                resumeChat();
                return;
            }

            /**
             * We've received an intent request to resume the existing chat.
             * Resume the chat via {@link com.zopim.android.sdk.api.ZopimChat#resume(android.support.v4.app.FragmentActivity)} and
             * start the {@link ZopimChatLogFragment}
             */
            if (getIntent() != null) {
                String action = getIntent().getAction();
                if (ChatActions.ACTION_RESUME_CHAT.equals(action)) {
                    resumeChat();
                    return;
                }
            }


            /**
             * Start a new chat
             */
            {
               /* // set pre chat fields as mandatory
                PreChatForm preChatForm = new PreChatForm.Builder()
                        .name(PreChatForm.Field.REQUIRED_EDITABLE)
                        .email(PreChatForm.Field.REQUIRED_EDITABLE)
                        .phoneNumber(PreChatForm.Field.REQUIRED_EDITABLE)
                        .build();*/
                // build chat config
                ZopimChat.SessionConfig config = new ZopimChat.SessionConfig();

                     /*   .preChatForm(preChatForm);*/
                /**
                 * Specify visitor data. This can be done at any point but it will apply at every chat startup.
                 */
                VisitorInfo visitorData = new VisitorInfo.Builder()
                        .name(Constants.GLOBAL_USER_NAME)
                        .phoneNumber(Constants.GLOBAL_USER_CONT_NO)
                        .email(Constants.GLOBAL_USER_EMAIL)
                        .build();

                ZopimChat.setVisitorInfo(visitorData);
                // prepare chat fragment
                ZopimChatFragment fragment = ZopimChatFragment.newInstance(config);

                // show fragment
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.chat_fragment_container, fragment, ZopimChatFragment.class.getName());
                transaction.commit();

            }
        }

        /**
         * Resumes the chat and loads the {@link ZopimChatLogFragment}
         */
        private void resumeChat() {

            FragmentManager manager = getSupportFragmentManager();
            // find the retained fragment
            if (manager.findFragmentByTag(ZopimChatLogFragment.class.getName()) == null) {
                ZopimChatLogFragment chatLogFragment = new ZopimChatLogFragment();

                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(com.zopim.android.sdk.R.id.chat_fragment_container, chatLogFragment, ZopimChatLogFragment.class.getName());
                transaction.commit();
            }
        }

        @Override
        public void onChatLoaded(Chat chat) {
            // TODO
        }

        @Override
        public void onChatInitialized() {

        }

        @Override
        public void onChatEnded() {
            // TODO
        }

        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onResume() {
            super.onResume();
            //resumeChat();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();

            if (progressDialog != null) {
                progressDialog.dismiss();
                // Constants.progressDialog = null;
            }

     }

        @Override
        public void onPause() {
            super.onPause();
        }

        @Override
        public void onStop() {
            super.onStop();
        }


        public void showProgressDialog(Activity ctx, String msg) {


            if (progressDialog == null) {
                progressDialog = new ProgressDialog(ctx);
            }

            progressDialog.getLayoutInflater();
            progressDialog.setMessage(msg);
            progressDialog.show();
        }

        public void dismissDialog() {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

        }

        ProgressDialog progressDialog;

    }

