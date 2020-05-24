# 過時版本放置區
###### tags: `程式專案`,`紫芋導向`,`start travel`
### alpha-1.1
> 版本資訊：
> -
> 
> **概述**
> 
> >將處理使用者的資訊完全交由資料庫處理
> >抽出**GUI處理類別**所有跟資料庫有關的語法並簡化使用者類別的責任
> >**UseInn**從萬能本地使用者管理員變成**只處理使用者認證機制**
> 
> **類別新增**
> - 新增package:util來裝一些設計模式的類別:命令模式、觀察者模式
> - 新增簡單的Exception：CommandException
> - 命令模式用來處理操作database資料的介面
> 
> | DatabaseInvoker Ⓒ| DBCommand Ⓐ| HanWen Ⓒ|
> | -------- | -------- | -------- |
> | 命令執行的管理者     | 命令(abstract)     | 命令執行者     |
> >值得注意的是只有Invoker有公開的方法可以調用，其餘預設是package private
> - 目前具體的命令類別有：
>    - 設定使用者 SetUserCommand
>    - 取得使用者 GetUserCommand (另外也是**具體的觀察主體concrete subject**)
>    - 新增訂單 AddOrderCommand
> - 操作展示：
>  ```java
>  DatabaseInvoker invoker = new DatabaseInvoker();
>  invoker.addCommand(new AddOrderCommand(new HanWen(),UID,new Order(travel)));
> invoker.assignCommand();
> ```
> - 觀察者模式用來處理database資料更新
> 
> | DBDataObserver Ⓘ | ~~Subject~~(目前並無介面) |
> | -------- | -------- |
> | 觀察者     | 觀察主體     |
> - 目前只有一個具體的觀察主體：GetUserCommand
> - 操作展示
> ```java
> NameDBObserver dbObserver = new NameDBObserver(userName);
> command.attach(dbObserver,NAME);
> ```
> **GUI處理類別更動**
> - ~~import Firebase.*~~ 移除對Firebase類別的依賴
> - 部分View會透過會傳參考物件給觀察者更新內容
>     - UserFragment中的TextView userName
>     - ListFragment中的recylerView以及getActivity()
> 
> **GUI資料更動**
> - 無
>
### alpha-1.2
> 版本資訊：
> -
> 
> **概述**
> 
> >升級各種資料以及建立「快速新增JSON field的程式」
> >搜尋frag更新至可以有限度獲得資料
> 
> **類別新增**
> - Travel資料升級
>      - 已訂人數
>      
> - Travel資料交由database處理並修改**travel_data package**
> - database新增ref:**raw** 用於存放**codes**、**travels**等重要資料
> - Order資料升級
>     - 成人訂購人數
>     - 孩童訂購人數
>     - 嬰兒訂購人數
>     - 訂單UID 使用**java.util.UUID**產生
> - 升級瀚文(命令執行者)的能力
> - 觀察主體的介面 **DBDataSubject**
> - 新增具體命令
>     - 添加原始清單命令 AddRawListCommand
>     - 取得旅遊結果命令 GetTravelsResultCommand
> - 新增具體觀察者
>     - 旅遊資料觀察者 TravelsDBObserver
>     
> **GUI處理類別更動**
> - (移植)**Travel處理者acti**:移到搜尋frag中
> - (修改)**搜尋frag**：具排序、各種篩選的選項
> 
> **GUI資料更動**
> - 移除無用的layout
> - 更新**搜尋frag**的layout
> - value/arrays:新增**下拉式選單**資料
### alpha-1.3
> 版本資訊：
> -
> 
> **概述**
> 
> >擴充**搜尋frag**至三種排序可能並新增選日期的GUI
> >擴充**使用者frag**至orders整合、個別order頁面
> 
> **類別新增**
> - 擴充Travel、Order資料: 實作**Serializable**介面使得**putExtras**可以傳
> - 從**TravelAdapter**拆離OrderList的處理
> - 新增**OrderAdapter**
> - 升級瀚文(命令執行者)的能力
> - GetTravelsResultCommand 擴充
>     
> **GUI處理類別更動**
> - (新增)**主frag**: 畫面動畫ViewAnimator
> - (新增)**搜尋frag**: 選日期功能
> - (新增)**選日frag**: DatePickerFragment
> - (新增)**咖波Alert**: 用於提醒視窗
> - (新增)**修改Alert**: 用於修改訂單
> - (新增)**取消Alert**: 用於取消訂單
> - (新增)**使用者訂單acti**: 顯示單筆訂單詳細資料
> - (修改)**使用者frag**: 完整化功能
> - (移植)**清單frag**:移到使用者frag中
> 
> **GUI資料更動**
> - 調整RecylerView相關的捲動問題:高度0dp
> - 調整旅遊單筆資料layout
> - 加入咖波Alert圖示
> - 多加一張「登入後layout」以及一張「訂單詳資layout」
> - 新增修改訂單Alertlayout
### alpha-1.4
> 版本資訊：
> -
> 
> *!!目前非預設選擇地區會是travel_code=396北歐的樣子!!*
> *!!選擇日期跟選擇地區目前只能擇一!!*
> 
> **概述**
> >資料庫移植至FireStore
> >改造處理資料庫的類別
> >調整搜尋frag至功能堪用
> 
> **類別更動**
> - 改裝瀚文為FireStore處理者
> - 將Observer跟CommandSubject都修正至FireStore模式
> - 新增User來接資料庫上的user資料
> - GetTravelsResultCommand改動至可以有八種排序或篩選的結果
> - ~~新增JSONSaver(Anodoroid Studio不支援寫檔案到電腦端)~~
>     
> **GUI處理類別更動**
> - (移植)**搜尋frag**: 詳細資訊以及訂購Alert移植
> - (新增)**Travel詳資acti**: 下方有訂購鈕
> 
> **GUI資料更動**
> - 加入一張TravelDetail layout
### alpha-1.5
> 版本資訊：
> -
> 
> **概述**
> > 新增即時抓資料圖片的方法
> > 修改選地區的命令至堪用
> > 導入AsyncTask
> 
> **類別更動**
> - 新增**ImageFromURLTask**: 用於以網址設定圖片
> - 微調瀚文的功能
> - 改造TravelAdapter: 提供static方法設置圖片
> - 調整GetTravelsResultCommand 的選擇地區
> - 建立訂單重新啟用(仍不會反應到已購人數)
> - 完成搜尋功能部份的acti生命週期控制
> - 改北韓國家名欄位為"bomb"以避免無搜尋結果
> - 新增具體命令
>     - 查找地區命令 LookForCodesCommand
>     
> **GUI處理類別更動**
> - (新增)**訂購acti**:訂購頁面(bar式選數量)
> - (新增)**讀取dia**: 用於讀取條顯示
> - 解決標題字太多的問題
> 
> **GUI資料更動**
> - 加入一張PurchaseForm layout
> - 加入seek bar layout
### alpha-1.6
> 版本資訊：
> -
> 
> **概述**
> > 新增彩蛋~~~~~~~~
> > 移植地區選擇至搜尋欄並完整化
> > 實作訂購、取消訂單
> 
> **類別更動**
> - 新增PlaceSuggestion
> - 新增PlaceCounselor
> - 新增CodeMapping以隔離Command跟Switch地區的複雜情形
> - 新增ExtirpateOrderCommand
> - 微調TravelAdapter讓無結果時顯示Alert
> - 修改AddOrderCommand
>     
> **GUI處理類別更動**
> - (新增)**彩蛋**
> - (修改)**搜尋frag**:篩選地區移植到搜尋欄、日期鈕長按取消
> - (修改)**訂購acti**:調整一些限制、例外
> - (新增)**動作完成anim**:成功訂購、移除以及~~修改~~
> 
> **GUI資料更動**
> - 加入彩蛋drawable
> - 加入seekbar_thumb
### alpha-1.7
> 版本資訊：
> -
> 
> **概述**
> > 修改訂單實作
> > 自定義Exception的處理
> 
> **類別更動**
> - 完成修改訂單的功能
> - 新增ReviseOrderCommand
> - Order微調以方便Command編寫
> - 補完小地區的選擇
> - 重構 LookForCodesCommand
> - CommandException的處理
>     - HW_NULL
>     - INPUT_INVALID
>     - NO_RESULT
>     
> **GUI處理類別更動**
> - (微調)**搜尋frag**:改排序到右上角
> 
> **GUI資料更動**
> - 加入ReviseOrder
### alpha-1.8
> 版本資訊：
> -
> 
> **概述**
> > 訂購系統引入時間、出團狀態系統
> 
> **類別更動**
> - 新增TravelStateOffice來判斷travel的狀態
>     - 搜尋端如果超過時間就不能訂
>     - 使用者訂單(列表)會顯示
>       「即將開始、進行中、已結束」
>     - 訂單若在即將開始前?天無法修改抑或取消(第四種狀態)
>     
> **GUI處理類別更動**
> - (微調)**搜尋frag**:顯示出團、時間狀態
> - (微調)**使用者frag**:顯示時間狀態
> - (微調)**旅遊資訊acti**:按鈕隨狀態更新
> - (微調)**訂單資訊acti**:按鈕隨狀態更新
> - 訂購畫面新增訂購提醒dialog
> 
> **GUI資料更動**
> - 微調旅遊資訊acti
> - 微調訂單資訊acti
> - 新增dialog_remind
> 
### alpha-1.9
> 版本資訊：
> -
> 
> **概述**
> > 首頁功能調整
> > 搜尋frag日期按鈕即時更新
> > 登入註冊流程控制
> 
> **類別更動**
> - 完成登入註冊功能的acti生命週期控制
> 
> **GUI處理類別更動**
> - (微調)**首頁frag**:連結搜尋frag跟熱門旅程acti
> - (新增)**首選行程acti**:簡單地用了個全螢幕畫面
> - (新增)**搜尋frag**:日期btn的addTextChangedListener移除im_btn
> - 搜尋frag新增搜尋提醒dialog
> - 新增登入註冊TextWatcher來管控btn
> 
> **GUI資料更動**
> - 更新主畫面資料
> - 首選行程layout
### beta-1.0
> 版本資訊：
> -
> 
> **概述**
> > Auth 讀寫資料庫設定
> > 清單GUI更新
> 
> **類別更動**
> - (新增)**OnOneOffClickListener**:解決listner聽兩次的狀況
> - (微調)**OrdersDBObserver**:吃進來的list移除「已經開始且沒到下限」的order
> - user/orders中的travel跟travels的同步
> 
> **GUI處理類別更動**
> - (微調)**userFrag**:登出功能
> 
> **GUI資料更動**
> - SearchFrag : 改篩選圖示、單項travel變矮、差幾人成團以及已經成團只剩多少名額、不能訂(人滿、過期)灰掉
> - TravelDetail ： 改layout