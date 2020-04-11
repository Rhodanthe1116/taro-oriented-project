# taro-oriented-project
## log
### alpha-1.5
> 版本資訊：
> -
> 
> **概述**
> >新增即時抓資料圖片的方法
> >修改選地區的命令至堪用
> >導入AsyncTask
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

--- 
> 開發方向
> - 
> - 萃取觀察者模式
> - Invoker實例是否提升層級
> - 整理程式碼 重構
> - Concrete Command 下一頁、選擇地區
>     - GetTravelsResultCommand 擴充
>     - 還有更多...
> - 咖波Alert處理Exception
> - 訂單系統完整化
> - GUI處理類別以及資料更新至4/6討論內容
>     - (修改)**搜尋frag**:依地區篩選、關鍵字搜尋
> - 畫面可以用好看一些~~
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
