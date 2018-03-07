import kotlinx.cinterop.*
import platform.Foundation.*
import platform.UIKit.*
import org.jetbrains.kotlin.common.httpclient.*

fun main(args: Array<String>) {
    memScoped {
        val argc = args.size + 1
        val argv = (arrayOf("konan") + args).map { it.cstr.getPointer(memScope) }.toCValues()

        autoreleasepool {
            UIApplicationMain(argc, argv, null, NSStringFromClass(AppDelegate))
        }
    }
}

class AppDelegate : UIResponder(), UIApplicationDelegateProtocol {
    companion object : UIResponderMeta(), UIApplicationDelegateProtocolMeta {}

    override fun init() = initBy(AppDelegate())

    private var _window: UIWindow? = null
    override fun window() = _window
    override fun setWindow(window: UIWindow?) {
        _window = window
    }
}


@ExportObjCClass
class ViewController : UIViewController {

    constructor(aDecoder: NSCoder) : super(aDecoder)

    override fun initWithCoder(aDecoder: NSCoder) = initBy(ViewController(aDecoder))

    @ObjCOutlet
    lateinit var label: UILabel

    @ObjCOutlet
    lateinit var textField: UITextField

    @ObjCOutlet
    lateinit var button: UIButton

    @ObjCAction
    fun buttonPressed() {
        performRequest(textField.text!!)
    }

    fun performRequest(endpoint: String) {
        val HEADER = "---==="
        val client = HttpClient()

        runSuspend {
            client.request {
                with(url) {
                    protocol = "https"
                    host = endpoint
                    port = 443
                }
            }
        }.then { response ->
            println("$HEADER request: ${response.request.url}")
            println("$HEADER response status: ${response.statusCode}")
            println("$HEADER headers:")
            response.headers.forEach { (key, values) ->
                println("  -$key: ${values.joinToString()}")
            }
            println("$HEADER body:")
            val body = Utils.decode(response.body, "windows-1251")

            println(body)
            label.text = body

            client.close()
        }
    }
}


