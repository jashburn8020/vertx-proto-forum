package com.jashburn.protoforum.storage;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jashburn.protoforum.model.MessageThread;

public class InMemoryStorageTest {

	private InMemoryStorage storage;
	
	@Before
	public void setUp() throws Exception {
		storage = new InMemoryStorage();
	}

	@After
	public void tearDown() throws Exception {
		// nothing to tear down.
	}

	@Test
	public void testStoreThread() {
		String title = "test title";
		MessageThread thread = new MessageThread();
		thread.setTitle(title);
		
		MessageThread storedThread = storage.storeThread(thread);
		assertEquals(title, storedThread.getTitle());
		assertNotNull(storedThread.getId());
	}

}
