# taro-oriented-project
## log
###### tags: `程式專案`,`紫芋導向`,`start travel`
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

--- 
> 開發方向
> - 
> - 萃取觀察者模式
> - Invoker實例是否提升層級
> - 整理程式碼 註解
> - 搜尋清單數量問題
> - orders中的travel要跟travels的同步
> - 使用者fragGUI優化
> - Auth 讀寫資料庫設定

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