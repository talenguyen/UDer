package com.tale.uder.tests;

import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tale.uder.R;
import com.tale.uder.TestUtils;
import com.tale.uder.rules.MockWebServerRule;
import com.tale.uder.rules.NeedsMockWebServer;
import com.tale.uder.screens.ItemsScreen;
import com.tale.uder.ui.main.MainActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class) public class ItemsTest {

  @Rule public RuleChain rules = RuleChain.emptyRuleChain()
      .around(new MockWebServerRule(this))
      .around(new ActivityTestRule<>(MainActivity.class));

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private ItemsScreen itemsScreen;

  @Before public void beforeEachTest() {
    itemsScreen = new ItemsScreen();
  }

  @Test // For real, it's really easy to break Toolbar, so why not test it?
  public void shouldDisplayTitle() {
    itemsScreen.shouldDisplayTitle(TestUtils.app().getString(R.string.app_name));
  }

  /**
   * @see #shouldDisplayAllItemsFromSuccessfulResponse_setupMockWebServer(MockWebServer)
   */
  @Test
  @NeedsMockWebServer(setupMethod = "shouldDisplayAllItemsFromSuccessfulResponse_setupMockWebServer")
  public void shouldDisplayAllItemsFromSuccessfulResponse() {
    itemsScreen.shouldDisplayExpectedAmountOfItems(8);

    for (int i = 1; i < 9; i++) {
      itemsScreen.shouldDisplayItemWithTitle("Test title " + i)
          .shouldDisplayItemWithShortDescription("Short desc " + i);
    }
  }

  /**
   * @see #shouldDisplayAllItemsFromSuccessfulResponse()
   */
  public void shouldDisplayAllItemsFromSuccessfulResponse_setupMockWebServer(
      @NonNull MockWebServer mockWebServer) {
    mockWebServer.enqueue(new MockResponse().setBody("["
        + "{ \"id\": \"test_id_1\", \"title\": \"Test title 1\", \"short_description\": \"Short desc 1\"},"
        + "{ \"id\": \"test_id_2\", \"title\": \"Test title 2\", \"short_description\": \"Short desc 2\"},"
        + "{ \"id\": \"test_id_3\", \"title\": \"Test title 3\", \"short_description\": \"Short desc 3\"},"
        + "{ \"id\": \"test_id_4\", \"title\": \"Test title 4\", \"short_description\": \"Short desc 4\"},"
        + "{ \"id\": \"test_id_5\", \"title\": \"Test title 5\", \"short_description\": \"Short desc 5\"},"
        + "{ \"id\": \"test_id_6\", \"title\": \"Test title 6\", \"short_description\": \"Short desc 6\"},"
        + "{ \"id\": \"test_id_7\", \"title\": \"Test title 7\", \"short_description\": \"Short desc 7\"},"
        + "{ \"id\": \"test_id_8\", \"title\": \"Test title 8\", \"short_description\": \"Short desc 8\"}"
        + "]"));
  }

  /**
   * @see #shouldDisplayErrorUiFrom500Response_setupMockWebServer(MockWebServer)
   */
  @Test @NeedsMockWebServer(setupMethod = "shouldDisplayErrorUiFrom500Response_setupMockWebServer")
  public void shouldDisplayErrorUiFrom500Response() {
    itemsScreen.shouldDisplayErrorText().shouldDisplayTryAgainButton().shouldNotDisplayItems();
  }

  /**
   * @see #shouldDisplayErrorUiFrom500Response()
   */
  public void shouldDisplayErrorUiFrom500Response_setupMockWebServer(
      @NonNull MockWebServer mockWebServer) {
    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 500 Not today"));
  }

  @Test
  @NeedsMockWebServer(setupMethod = "shouldDisplayErrorUiThenReloadResultSuccessfully_setupMockWebServer")
  public void shouldDisplayErrorUiThenReloadResultSuccessfully() {
    itemsScreen.shouldDisplayErrorText()
        .shouldDisplayTryAgainButton()
        .shouldNotDisplayItems()
        .clickOnTryAgainButton()
        .shouldDisplayExpectedAmountOfItems(3)
        .shouldDisplayItemWithTitle("Loaded successfully 1")
        .shouldDisplayItemWithShortDescription("Short success desc 1")
        .shouldDisplayItemWithTitle("Loaded successfully 2")
        .shouldDisplayItemWithShortDescription("Short success desc 2")
        .shouldDisplayItemWithTitle("Loaded successfully 3")
        .shouldDisplayItemWithShortDescription("Short success desc 3");
  }

  public void shouldDisplayErrorUiThenReloadResultSuccessfully_setupMockWebServer(
      @NonNull MockWebServer mockWebServer) {
    mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 500 Wanna try again?"));
    mockWebServer.enqueue(new MockResponse().setBody("["
        + "{ \"id\": \"test_id_1\", \"title\": \"Loaded successfully 1\", \"short_description\": \"Short success desc 1\"},"
        + "{ \"id\": \"test_id_2\", \"title\": \"Loaded successfully 2\", \"short_description\": \"Short success desc 2\"},"
        + "{ \"id\": \"test_id_3\", \"title\": \"Loaded successfully 3\", \"short_description\": \"Short success desc 3\"}"
        + "]"));
  }
}
