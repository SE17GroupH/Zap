# Zap

An Android application showcasing 3 methods of authentication:
- Fingerprint
- Voice via Speaker Recognition
- via Token(or Magic) link 

To use Microsoft API for Speaker Recongition:
- Create a SubKey.java file with contents these contents with your Subscription Keys [generated](https://cognitive.uservoice.com/knowledgebase/articles/864225-how-do-i-get-subscription-keys):

            package teamh.zapapp;
            public class SubKey {
                        public final static String sub_key1 = "abcd123abc123abc123abc123abc0000";
                        public final static String sub_key2 = "abcd123abc123abc123abc123abc0000";
            }
