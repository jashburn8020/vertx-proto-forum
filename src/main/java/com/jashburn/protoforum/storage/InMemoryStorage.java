package com.jashburn.protoforum.storage;

import com.jashburn.protoforum.model.Forum;

public class InMemoryStorage implements Storage {

	private Forum forum;

	public InMemoryStorage() {
		forum = new Forum("ProtoForum", "Simple prototype of a forum");
	}

	@Override
	public Forum getForum() {
		return forum;
	}
}
