# taro-oriented-project
## log
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

--- 
> 開發方向
> - 
> - 資料庫移植至FireStore
> - 萃取觀察者模式
> - 改裝瀚文為FireStore處理者
> - Invoker實例是否提升層級
> - Concrete Command設計並開發
>     - GetTravelsResultCommand 擴充
>     - 還有更多...
> - 等待畫面
> - 咖波Alert處理Exception
> - GUI處理類別以及資料更新至4/6討論內容
>     - (修改)**搜尋frag**:持續改進
>     - (新增)**Travel詳資acti**:下方有訂購鈕
>     - (新增)**訂購acti**:訂購頁面
>     - (移植)**Travel處理者acti**:移到搜尋frag中
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

