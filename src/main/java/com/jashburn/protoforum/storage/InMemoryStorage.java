package com.jashburn.protoforum.storage;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.jashburn.protoforum.model.Forum;
import com.jashburn.protoforum.model.MessageThread;

public class InMemoryStorage implements Storage {

	private Forum forum;
	
	private Map<UUID, MessageThread> threads;

	public InMemoryStorage() {
		forum = new Forum("ProtoForum", "Simple prototype of a forum");
		threads = new LinkedHashMap<>();
	}

	@Override
	public Forum getForum() {
		return forum;
	}

	@Override
	public MessageThread storeThread(MessageThread thread) {
		UUID threadId = UUID.randomUUID();
		thread.setId(threadId);

		threads.put(threadId, thread);

		return thread;
	}
}
