package io.github.lumue.woelkchen.download.sites.xh

import io.github.lumue.woelkchen.download.AbstractLoginTest
import io.github.lumue.woelkchen.download.BasicHttpClient
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before
import org.junit.Test

class XhLoginTest : AbstractLoginTest() {



    @Before
    fun initClient() {
        client = XhHttpClient("dirtytom74", "ddl85s")
    }


}