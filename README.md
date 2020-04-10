# taro-oriented-project
## log
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

--- 
> 開發方向
> - 
> - 萃取觀察者模式
> - Invoker實例是否提升層級
> - code(int)、country(String)互相轉換的方法
> - Concrete Command 下一頁、選擇地區
>     - GetTravelsResultCommand 擴充
>     - 還有更多...
> - 等待畫面
> - 實作訂單的資料流動
> - 從網址設置圖片資源的方法
> - 咖波Alert處理Exception
> - GUI處理類別以及資料更新至4/6討論內容
>     - (修改)**搜尋frag**:依地區篩選、關鍵字搜尋
>     - (新增)**訂購acti**:訂購頁面(bar式選數量)
> - 畫面可以用好看一些~~
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
