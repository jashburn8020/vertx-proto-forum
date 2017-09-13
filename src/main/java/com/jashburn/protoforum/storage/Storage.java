package com.jashburn.protoforum.storage;

import com.jashburn.protoforum.model.Forum;
import com.jashburn.protoforum.model.MessageThread;

public interface Storage {

	Forum getForum();

	MessageThread storeThread(MessageThread thread);

}
