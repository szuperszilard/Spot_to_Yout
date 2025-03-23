import puppeteer from 'puppeteer-extra';
import fs from "fs/promises";
import StealthPlugin from "puppeteer-extra-plugin-stealth";
import AnonymizeUAPlugin from "puppeteer-extra-plugin-anonymize-ua";
import AdblockerPlugin from "puppeteer-extra-plugin-adblocker";

const stealth = StealthPlugin();
stealth.enabledEvasions.delete('iframe.contentWindow');
stealth.enabledEvasions.delete('media.codecs');
puppeteer.use(stealth);
puppeteer.use(AnonymizeUAPlugin());
puppeteer.use(AdblockerPlugin({ blockTrackers: true }));

console.time("main");

async function main(songsFilePath = 'test.txt', title = "Grunge'n'shit") {

    const browser = await puppeteer.launch({
        headless: false,
        args: ['--no-sandbox', '--disable-setuid-sandbox', '--window-size=1024x1024', '--disable-infobars']
    });
    let searchCount = 0;
    let lineNumber = 1;
    const ytMusicSearchBoxSelector = '.ytmusic-search-box';
    const librarySelector = '::-p-xpath(/html/body/ytmusic-app/ytmusic-app-layout/div[4]/ytmusic-guide-renderer/div/ytmusic-guide-section-renderer[1]/div[2]/ytmusic-guide-entry-renderer[3]/tp-yt-paper-item)';
    const playlistsButtonSelector = 'iron-selector > ytmusic-chip-cloud-chip-renderer:has(a[title="Show playlists"]) > div > a'
    const newPlaylistButtonSelector = '::-p-xpath(/html/body/ytmusic-app/ytmusic-app-layout/div[5]/ytmusic-browse-response/div[2]/div[4]/ytmusic-section-list-renderer/div[2]/ytmusic-grid-renderer/div[2]/ytmusic-two-row-item-renderer[1]/div[1]/div/yt-formatted-string/a)';
    const newPlaylistInputSelector = '::-p-xpath(/html/body/ytmusic-dialog/tp-yt-paper-dialog-scrollable/div/div/ytmusic-playlist-form/tp-yt-iron-pages/div[1]/div[1]/tp-yt-paper-input/tp-yt-paper-input-container/div[2]/div/tp-yt-iron-input/input)';
    const newPlaylistTypeSelector = '::-p-xpath(/html/body/ytmusic-dialog/tp-yt-paper-dialog-scrollable/div/div/ytmusic-playlist-form/tp-yt-iron-pages/div[1]/div[1]/tp-yt-paper-input)';
    const newPlaylistCreateButtonSelector = '::-p-xpath(/html/body/ytmusic-dialog/tp-yt-paper-dialog-scrollable/div/div/ytmusic-playlist-form/tp-yt-iron-pages/div[1]/div[2]/yt-button-renderer[2])';
    const songsButtonSelector = 'iron-selector > ytmusic-chip-cloud-chip-renderer:has(a[title="Show song results"]) > div > a';
    const firstSongRightClickSelector = '::-p-xpath(/html/body/ytmusic-app/ytmusic-app-layout/div[5]/ytmusic-search-page/ytmusic-tabbed-search-results-renderer/div[2]/ytmusic-section-list-renderer/div[2]/ytmusic-shelf-renderer/div[3]/ytmusic-responsive-list-item-renderer[1]/div[2]/div[1]/yt-formatted-string/a)';
    const firstSongAddToPlaylistSelector = '::-p-xpath(/html/body/ytmusic-app/ytmusic-popup-container/tp-yt-iron-dropdown/div/ytmusic-menu-popup-renderer/tp-yt-paper-listbox/ytmusic-menu-navigation-item-renderer[2])';
    const whichPlaylistSelector = '::-p-xpath(/html/body/ytmusic-app/ytmusic-popup-container/tp-yt-paper-dialog/ytmusic-add-to-playlist-renderer/div[2]/div[1]/ytmusic-carousel-shelf-renderer/div/ytmusic-carousel/div/ul/ytmusic-two-row-item-renderer[1]/a)'
    const searchBoxXSelector = '::-p-xpath(/html/body/ytmusic-app/ytmusic-app-layout/ytmusic-nav-bar/div[2]/ytmusic-search-box/div/div[1]/yt-icon-button[2]/button)';

    //open page
    let page = await browser.newPage();
    await page.goto('https://music.youtube.com/');
    await page.click(".fBCs6c");
    await page.waitForNavigation();

    //click on Library
    await (await page.waitForSelector(librarySelector)).click();
    await page.waitForNavigation();

    //click on Playlists
    await (await page.waitForSelector(playlistsButtonSelector)).click();
    await page.waitForNavigation();

    //create new playlist
    await (await page.waitForSelector(newPlaylistButtonSelector)).click();
    await (await page.waitForSelector(newPlaylistInputSelector)).click();
    await page.type(newPlaylistTypeSelector, title);
    await (await page.waitForSelector(newPlaylistCreateButtonSelector)).click();
    await page.waitForNavigation();

    function sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    async function searchAndClick(songTITLE){
        await page.waitForSelector(ytMusicSearchBoxSelector);
        await page.click(ytMusicSearchBoxSelector);
        await page.type(ytMusicSearchBoxSelector, songTITLE);
        await page.keyboard.press("Enter");
        await page.waitForNavigation();
        await page.waitForSelector(songsButtonSelector, {visible: true, timeout: 20000});
        const elm = await page.$(songsButtonSelector);
        await elm.click();
        await page.waitForNavigation();
    }

    async function firstSongToPlaylist(songTitle){
        await searchAndClick(songTitle);
        await (await page.waitForSelector(firstSongRightClickSelector)).click({button: "right"});
        await (await page.waitForSelector(firstSongAddToPlaylistSelector)).click();
        await (await page.waitForSelector(whichPlaylistSelector)).click();
        await page.waitForSelector(searchBoxXSelector);
        await page.click(searchBoxXSelector);
    }

    async function readFromFile(songsFilePath) {

        const data = await fs.readFile(songsFilePath, 'utf8');
        const lines = data.split('\n');
        await firstSongToPlaylist(lines[0]);
        let dontExecute = false;

            for (let i = 0; i < lines.length; i++) {

                if(searchCount === 100){
                    dontExecute = true;
                    await page.close();
                    page = await browser.newPage();
                    await page.goto('https://music.youtube.com/');
                    await firstSongToPlaylist(lines[i]);
                    searchCount = 0;
                }
                if(lineNumber === 1 || dontExecute === true){
                }else {
                    await searchAndClick(lines[i]);
                    await (await page.waitForSelector(firstSongRightClickSelector)).click({button: "right"});
                    await (await page.waitForSelector(firstSongAddToPlaylistSelector)).click();
                    if(searchCount === 99){
                        await sleep(500);
                    }
                    if(i !== lines.length - 1){
                        await (await page.waitForSelector(searchBoxXSelector)).click();
                    }
                }
                if(i === lines.length - 1){
                    await sleep(1000);
                }
                lineNumber++;
                searchCount++;
                dontExecute = false;
            }
    }
    await readFromFile(songsFilePath);
    await browser.close();
}
const songsFile = process.argv[2];
const titleOfPlaylist = process.argv[3];
await main(songsFile, titleOfPlaylist);


console.timeEnd("main");