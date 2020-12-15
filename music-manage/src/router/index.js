import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login'
import Home from "../views/Home";
import InfoPage from "../components/InfoPage";
import UserPage from "../components/UserPage";
import SingerPage from "../components/SingerPage";
import SongListPage from "../components/SongListPage";
import SongPage from "../components/SongPage";
import ListSongPage from "../components/ListSongPage";

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        component: Login
    },
    {
        path: '/Home',
        component: Home,
        redirect: '/Info',
        children: [
            {
                path: '/Info',
                component: InfoPage
            },
            {
                path: '/User',
                component: UserPage
            },
            {
                path: '/Singer',
                component: SingerPage
            },
            {
                path: '/SongList',
                component: SongListPage
            },
            {
                name: 'SongPage',
                path: '/SongPage/:id',
                component: SongPage,
            },
            {
                name: 'ListSongPage',
                path: '/ListSong/:id',
                component: ListSongPage
            }
        ]
    }
]

const router = new VueRouter({
    mode: 'history',
    routes
})

export default router
